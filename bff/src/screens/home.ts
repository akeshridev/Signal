import { Router } from "express";
import { getTokens, Token } from "../data/tokens";

export const homeScreenRouter = Router();

interface ActionButton {
  label: string;
  action: string;
}

/**
 * Three action states, keyed off 24h price direction:
 *  - up:   "Explain this move" -> explain (LLM wording leans positive)
 *  - down: "Why is this down?" -> explain (LLM wording leans negative)
 *  - flat/unknown: "Compare" -> compare (fixed label, no LLM involved)
 */
function actionButtonFor(change: number | null): ActionButton {
  if (change === null || change === 0) return { label: "Compare", action: "compare" };
  if (change > 0) return { label: "Explain this move", action: "explain" };
  return { label: "Why is this down?", action: "explain" };
}

function paramsFor(token: Token): Record<string, unknown> {
  if (token.change_24h === null || token.change_24h === 0) return { token: token.name };
  return {
    token: token.name,
    direction: token.change_24h > 0 ? "up" : "down",
    // Grounds the LLM in this token's actual numbers instead of letting it
    // invent plausible-sounding market commentary from the % change alone.
    volume: token.volume_display,
    range: token.range_display,
  };
}

homeScreenRouter.get("/screen/home", async (_req, res) => {
  const tokens = await getTokens();

  const components = tokens.map((token: Token) => ({
    type: "token_card",
    data: {
      name: token.name,
      symbol: token.symbol,
      price_display: token.price_display,
      insight: token.insight,
      change_24h: token.change_24h,
      icon_url: token.icon_url,
      rank: token.rank,
      ai_action_button: {
        ...actionButtonFor(token.change_24h),
        params: paramsFor(token),
      },
    },
  }));

  res.json({
    screen_id: "home",
    components,
  });
});
