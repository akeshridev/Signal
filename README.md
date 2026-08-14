# Signal

A server-driven UI demo: an Android client renders component trees it doesn't understand ahead of time, served by a backend-for-frontend that mixes static token data with AI-generated insights.

## Structure

- **`bff/`** — Express + TypeScript backend-for-frontend. Serves screens as JSON component trees (`GET /screen/:screenId`) and handles AI-backed actions (`POST /action/:action`), e.g. asking an LLM to explain a token's price move.
- **`mobile/`** — Android client (Kotlin, Jetpack Compose). Renders any component whose `type` appears in the shared schema without hardcoding what each screen contains.
- **`shared/component-schema.json`** — Single source of truth for the component-tree contract. Both `bff/` and `mobile/` must stay in sync with it.

## Running the BFF

```bash
cd bff
cp .env.example .env   # fill in LLM_PROVIDER / LLM_API_KEY, or leave blank for the templated fallback
npm install
npm run dev             # http://localhost:3000
```

## Running the mobile app

Open `mobile/` in Android Studio and run the `app` module on an emulator. The client points at `http://10.0.2.2:3000/` (the emulator's alias for the host machine), matching the BFF's default port — see `BffApiClient` in `mobile/app/src/main/java/com/ashish/signal/data/network/BffApi.kt`.

## Contract

Components are typed nodes (`token_card`, `bottom_sheet` today) validated against `shared/component-schema.json`. Adding a new component type means updating the schema, the BFF response shape, and adding a matching renderer on the mobile side.
