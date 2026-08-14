import tokensFixture from "./tokens.fixture.json";

export interface Token {
  name: string;
  symbol: string;
  price_display: string;
  insight: string;
  /** Raw 24h % change, null when unavailable. Drives the home screen's
   *  per-card action button state (explain up/down vs. compare) and the
   *  card's ticker-style change readout. */
  change_24h: number | null;
  /** Hosted logo URL from CoinGecko; null falls back to the monogram avatar. */
  icon_url: string | null;
  /** Market cap rank (1 = largest), null when unavailable. */
  rank: number | null;
  /** Compact 24h trading volume, e.g. "$28.4B" — grounds /action/explain in a
   *  real per-token number instead of letting the LLM invent market color. */
  volume_display: string | null;
  /** 24h price range, e.g. "$61,203.45 – $63,412.10" — same grounding purpose. */
  range_display: string | null;
}

const TOKEN_LIMIT = 20;
const COINGECKO_URL =
  `https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd` +
  `&order=market_cap_desc&per_page=${TOKEN_LIMIT}&page=1&price_change_percentage=24h`;

interface CoinGeckoMarket {
  name: string;
  symbol: string;
  image: string;
  market_cap_rank: number | null;
  current_price: number;
  price_change_percentage_24h: number | null;
  total_volume: number | null;
  high_24h: number | null;
  low_24h: number | null;
}

function formatPrice(price: number): string {
  return price.toLocaleString("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: price < 1 ? 4 : 2,
    maximumFractionDigits: price < 1 ? 4 : 2,
  });
}

function formatCompactVolume(volume: number | null): string | null {
  if (volume === null) return null;
  return volume.toLocaleString("en-US", {
    style: "currency",
    currency: "USD",
    notation: "compact",
    maximumFractionDigits: 1,
  });
}

function formatRange(low: number | null, high: number | null): string | null {
  if (low === null || high === null) return null;
  return `${formatPrice(low)} – ${formatPrice(high)}`;
}

function insightFor(change: number | null): string {
  if (change === null) return "Price change data unavailable right now.";
  const direction = change >= 0 ? "Up" : "Down";
  return `${direction} ${Math.abs(change).toFixed(1)}% in the last 24h.`;
}

function fixtureTokens(): Token[] {
  return tokensFixture.tokens;
}

/**
 * Live token list from CoinGecko's free public market-data API, falling
 * back to the static fixture (never an error) when the request fails.
 */
export async function getTokens(): Promise<Token[]> {
  try {
    const res = await fetch(COINGECKO_URL);
    if (!res.ok) return fixtureTokens();

    const markets = (await res.json()) as CoinGeckoMarket[];
    if (!Array.isArray(markets) || markets.length === 0) return fixtureTokens();

    return markets.map((market) => ({
      name: market.name,
      symbol: market.symbol.toUpperCase(),
      price_display: formatPrice(market.current_price),
      insight: insightFor(market.price_change_percentage_24h),
      change_24h: market.price_change_percentage_24h,
      icon_url: market.image || null,
      rank: market.market_cap_rank,
      volume_display: formatCompactVolume(market.total_volume),
      range_display: formatRange(market.low_24h, market.high_24h),
    }));
  } catch {
    return fixtureTokens();
  }
}