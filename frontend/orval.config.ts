import { defineConfig } from "orval";

// https://orval.dev/reference/configuration/overview
export default defineConfig({
  petstore: {
    output: {
      target: "src/api/api.ts",
      client: "axios",
      schemas: "src/api/types",
      mode: "tags",
      mock: false,
      prettier: true,
      clean: true,
      override: {
        mutator: {
          path: './src/lib/axios-instance.ts',
          name: 'axiosInstance',
        },
      },
    },
    input: {
      target: "http://localhost:8080/v3/api-docs",
      // validation: true,
    },
  },
});
