import "dotenv/config";
import express from "express";
import { homeScreenRouter } from "./screens/home";
import { explainActionRouter } from "./actions/explain";
import { compareActionRouter } from "./actions/compare";

const app = express();

app.use(express.json());
app.use(homeScreenRouter);
app.use(explainActionRouter);
app.use(compareActionRouter);

const port = Number(process.env.PORT) || 3000;

app.listen(port, () => {
  console.log(`signal-bff listening on http://localhost:${port}`);
});
