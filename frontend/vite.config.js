import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
  server: {
    proxy: {
      '/api': {
        target: 'http://20.41.106.213:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api'),
      },
      // '/ws-stomp': {
      //   target: 'http://localhost:8080',
      //   changeOrigin: true,
      //   ws: true,
      // }
    },
  },
  define: {
    global: {},
  }
});
