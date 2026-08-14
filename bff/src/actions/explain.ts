import { Router } from "express";
import { explainToken } from "../llm/client";

export const explainActionRouter = Router();

explainActionRouter.post("/action/explain", async (req, res) => {
  const token = typeof req.body?.token === "string" ? req.body.token : undefined;
  const direction = req.body?.direction === "down" ? "down" : "up";
  const iconUrl = typeof req.body?.icon_url === "string" && req.body.icon_url ? req.body.icon_url : null;
  const volume = typeof req.body?.volume === "string" ? req.body.volume : null;
  const range = typeof req.body?.range === "string" ? req.body.range : null;

  if (!token) {
    res.status(400).json({ error: "token is required" });
    return;
  }

  const body = await explainToken(token, direction, { volume, range });

  res.json({
    component: {
      type: "bottom_sheet",
      data: {
        title: `About ${token}`,
        body,
        close_label: "Got it",
        token,
        icon_url: iconUrl,
        ai_generated: true,
      },
    },
  });
});
