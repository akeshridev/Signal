import { Router } from "express";

export const compareActionRouter = Router();

/**
 * Fixed-wording action for flat/unknown-direction tokens — there's no price
 * move to explain, so this offers a relative comparison instead. Unlike
 * /action/explain, this never calls the LLM: the copy isn't context-dependent.
 */
compareActionRouter.post("/action/compare", (req, res) => {
  const token = typeof req.body?.token === "string" ? req.body.token : undefined;
  const iconUrl = typeof req.body?.icon_url === "string" && req.body.icon_url ? req.body.icon_url : null;

  if (!token) {
    res.status(400).json({ error: "token is required" });
    return;
  }

  res.json({
    component: {
      type: "bottom_sheet",
      data: {
        title: `Compare ${token}`,
        body: `${token} hasn't made a clear move in the last 24h. Check how it's performing against the other tokens in your list to see if it's leading or lagging the broader market.`,
        close_label: "Got it",
        token,
        icon_url: iconUrl,
        ai_generated: false,
      },
    },
  });
});
