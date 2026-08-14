import fs from "fs";
import path from "path";
import Anthropic from "@anthropic-ai/sdk";

const EXPLAIN_PROMPT_PATH = path.join(__dirname, "prompts", "explain.prompt.txt");
const EXPLAIN_PROMPT_TEMPLATE = fs.readFileSync(EXPLAIN_PROMPT_PATH, "utf-8");

// Meta's Llama API and Groq's API are both OpenAI-compatible; all four are
// overridable in case the account uses a different region/host/model tier
// than these defaults.
const META_BASE_URL = process.env.LLM_BASE_URL || "https://api.meta.ai/v1/chat/completions";
const META_MODEL = process.env.LLM_MODEL || "muse-spark-1.2";

const GROQ_BASE_URL = process.env.LLM_BASE_URL || "https://api.groq.com/openai/v1/chat/completions";
const GROQ_MODEL = process.env.LLM_MODEL || "llama-3.3-70b-versatile";

export interface ExplainContext {
  volume: string | null;
  range: string | null;
}

function renderPrompt(template: string, vars: Record<string, string>): string {
  return template.replace(/\{\{(\w+)\}\}/g, (_match, key) => vars[key] ?? "");
}

/**
 * Turns whatever real numbers we have into a grounding line for the prompt.
 * Without this, the LLM has only the price-direction word to go on and fills
 * the rest with plausible-sounding but generic finance-speak — the same
 * "confidently generic" failure mode as hallucinating on missing signal.
 */
function contextLine(context: ExplainContext): string {
  const parts: string[] = [];
  if (context.volume) parts.push(`24h trading volume is ${context.volume}`);
  if (context.range) parts.push(`today's price range is ${context.range}`);
  if (parts.length === 0) return "";
  return `Real data for this token right now: ${parts.join("; ")}.`;
}

function templatedFallback(token: string, direction: "up" | "down", context: ExplainContext): string {
  const trend = direction === "up" ? "upward" : "downward";
  const dataNote = context.volume && context.range
    ? ` Today's range has been ${context.range} on ${context.volume} in volume.`
    : "";
  return `${token}'s recent ${trend} move reflects normal market movement.${dataNote} We couldn't reach the AI explainer just now, so here's the short version: check the token's recent volume and news before reading too much into short-term swings.`;
}

async function callAnthropic(prompt: string, apiKey: string): Promise<string | null> {
  const client = new Anthropic({ apiKey });

  const response = await client.messages.create({
    model: "claude-opus-5",
    max_tokens: 300,
    messages: [{ role: "user", content: prompt }],
  });

  if (response.stop_reason === "refusal") return null;

  const textBlock = response.content.find((block) => block.type === "text");
  if (!textBlock || textBlock.type !== "text" || !textBlock.text.trim()) return null;

  return textBlock.text.trim();
}

/** Shared caller for OpenAI-compatible chat-completions endpoints (Meta, Groq). */
async function callOpenAiCompatible(
  baseUrl: string,
  model: string,
  maxTokensParam: "max_tokens" | "max_completion_tokens",
  prompt: string,
  apiKey: string
): Promise<string | null> {
  const res = await fetch(baseUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${apiKey}`,
    },
    body: JSON.stringify({
      model,
      messages: [{ role: "user", content: prompt }],
      [maxTokensParam]: 300,
    }),
  });

  if (!res.ok) return null;

  const json = (await res.json()) as any;

  // Some providers (e.g. Meta's native Llama API shape) nest text here.
  const nativeText = json?.completion_message?.content?.text;
  if (typeof nativeText === "string" && nativeText.trim()) return nativeText.trim();

  // Standard OpenAI-compatible response shape.
  const compatText = json?.choices?.[0]?.message?.content;
  if (typeof compatText === "string" && compatText.trim()) return compatText.trim();

  return null;
}

/**
 * Calls the configured LLM provider (LLM_PROVIDER: "anthropic" | "meta" |
 * "groq") to explain a token, falling back to a templated response (never
 * an error) when LLM_API_KEY is missing, the provider is unsupported, or
 * the call fails for any reason.
 */
export async function explainToken(
  token: string,
  direction: "up" | "down" = "up",
  context: ExplainContext = { volume: null, range: null }
): Promise<string> {
  const provider = process.env.LLM_PROVIDER;
  const apiKey = process.env.LLM_API_KEY;

  if (!apiKey) return templatedFallback(token, direction, context);

  const prompt = renderPrompt(EXPLAIN_PROMPT_TEMPLATE, { token, direction, context: contextLine(context) });

  try {
    let result: string | null = null;

    if (provider === "anthropic") {
      result = await callAnthropic(prompt, apiKey);
    } else if (provider === "meta") {
      result = await callOpenAiCompatible(META_BASE_URL, META_MODEL, "max_completion_tokens", prompt, apiKey);
    } else if (provider === "groq") {
      result = await callOpenAiCompatible(GROQ_BASE_URL, GROQ_MODEL, "max_tokens", prompt, apiKey);
    }

    return result ?? templatedFallback(token, direction, context);
  } catch {
    return templatedFallback(token, direction, context);
  }
}
