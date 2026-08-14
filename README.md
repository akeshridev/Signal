# Signal

> What if the mobile client had zero presentation logic?

Signal is a server-driven UI experiment: an Android client that renders component trees it has no prior knowledge of, served by a mobile backend-for-frontend (BFF) that mixes deterministic market data with selectively-applied LLM reasoning.

The client doesn't know what a "Bitcoin" or a "downtrend" is. It knows how to render **10 primitives** — Text, Card, List, Button, BottomSheet — and dispatch intents like `Ask AI`. Everything else — what to show, what it says, when to call an LLM — is decided server-side and shipped down as JSON.

## The hypothesis

In standard MVVM, the ViewModel lives on the device. Here, the ViewModel factory and all presentation logic live in the Mobile BFF instead. The client becomes a generic renderer for a `ServerViewState`, nothing more.

**Why this matters**: a new screen, new copy, or new logic is a backend deploy — not an app store release. One logic layer serves both iOS and Android, since neither client owns any presentation decisions to duplicate.

**Why this is still an experiment, not a production template**: a fully thin client is idealistic. In production this would be a hybrid — local caching for offline resilience and latency, not a pure server-round-trip on every interaction. See [Caching](#caching--offline) below for how this repo actually handles that tension.

## Architecture

```
User taps "Ask AI" on Bitcoin ($62,859)
        │
        ▼
Client dispatches intent → POST /action/explain
        │
        ▼
BFF fetches market data (price, 24h volume, range)
        │
        ▼
BFF calls the LLM with a strict JSON schema —
NOT "write me some UI text", but:
{ summary, volume, range, sentiment }
        │
        ▼
BFF validates the LLM's structured output
        │  (invalid or failed → safe templated fallback, never an error)
        ▼
BFF assembles the final ServerViewState (bottom_sheet)
        │
        ▼
Client renders it — it never talks to the LLM directly
```

## Structure

- **`bff/`** — Express + TypeScript backend-for-frontend.
  - `GET /screen/:screenId` — returns a screen as a component-tree `ServerViewState`.
  - `POST /action/:action` — handles AI-backed actions (`explain`, `compare`; `risk-check` not yet implemented).
  - `src/llm/client.ts` — the only place that talks to an LLM. Reads `LLM_PROVIDER` / `LLM_API_KEY` from env.
  - `src/llm/prompts/` — prompt templates, kept separate from route code so wording can be tuned without touching logic.
- **`mobile/`** — Android client (Kotlin, Jetpack Compose).
  - `ui/components/ComponentRenderer.kt` — the *only* place that branches on a component's `type`. `HomeScreen.kt` and `HomeViewModel.kt` have zero knowledge of what a `token_card` or `bottom_sheet` even is.
  - `data/network/dto/ComponentDto.kt` — generic `{ type, data: Map<String, Any> }`, so a new component type never requires a DTO change.
- **`shared/component-schema.json`** — single source of truth for the component-tree contract. `bff/` and `mobile/` both implement against this; it's the thing that keeps them from silently drifting apart.

## The AI discipline rule

The LLM is used for **language and judgment** — never for **exact computation**.

| Deterministic (BFF code, always) | LLM (only where judgment is required) |
|---|---|
| Currency conversion, number formatting | Explaining *why* a price moved |
| Sorting, filtering the token list | Comparing a token against the broader list |
| Which component *type* to render | Writing the natural-language summary |

The LLM never generates UI directly. It generates structured state (`{ summary, volume, range, sentiment }`), which the BFF validates against a schema before assembling the actual response. If validation fails or the LLM call errors out, the BFF returns a safe fallback `ServerViewState` — the client never sees an error, and it never talks to the LLM directly at all.

## Caching & offline

The client caches the last `ServerViewState` per screen locally (Room), keyed by screen/action identifier, alongside a fetch timestamp and a TTL — where the **TTL itself is set by the BFF's response**, not hardcoded client-side. Volatile data (live prices) gets a short TTL; low-volatility data would get a much longer one, without any client code change.

Flow on screen open:
1. Render from local cache immediately, if present — no blank/loading flash on a warm cache.
2. If the cache is stale (past its TTL), fire a non-blocking background fetch.
3. On success, silently update the cache and diff the UI — no reload flash.
4. If the BFF is unreachable, keep showing the last-known cache (clearly marked as stale) rather than clearing the screen, and disable AI actions rather than letting a tap hang.

## Running locally

**BFF:**
```bash
cd bff
cp .env.example .env   # fill in LLM_PROVIDER / LLM_API_KEY, or leave blank for the templated fallback
npm install
npm run dev             # http://localhost:3000
```

**Mobile:**
Open `mobile/` in Android Studio and run the `app` module on an emulator. The client points at `http://10.0.2.2:3000/` (the emulator's alias for the host machine) by default — see `BffApi.kt`.

## Known limitations

- **Risk-check** is designed (see the use-case doc) but not yet implemented — only `explain` and `compare` are live.
- This is a concept app for exploring server-driven UI + MVVM + selective LLM injection. **It is not financial advice**, and insights shown in the app are AI-generated and may be inaccurate — the UI includes an explicit "Generated by AI — may be inaccurate" disclaimer for exactly this reason.
- A fully thin client is a deliberate simplification for this experiment. Any production version of this pattern would need the caching/offline layer described above to be load-bearing, not optional — which is why it's implemented here rather than left as a "future work" note.

## Open question

Where do you think this pattern breaks in a real production app? Issues and discussion welcome.
