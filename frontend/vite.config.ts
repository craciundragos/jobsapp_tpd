import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            '/api': {
                target: 'http://jobsapp-alb-842034691.eu-central-1.elb.amazonaws.com',
                changeOrigin: true,
                secure: false,
                timeout: 300000,
                proxyTimeout: 300000
            },
        },
    },
})