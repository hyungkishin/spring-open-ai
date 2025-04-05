import { UserConfigFn } from 'vite';
import { overrideVaadinConfig } from './vite.generated';
import { defineConfig } from 'vite';
// import react from '@vitejs/plugin-react-swc';
import { visualizer } from 'rollup-plugin-visualizer';

const customConfig: UserConfigFn = (env) => ({
    plugins: [
        // react(), // SWC 기반 React 플러그인 (Babel 대비 70% 빠른 빌드)
        visualizer({
            open: true, // 빌드 후 번들 분석 보고서 자동 오픈
            gzipSize: true,
            brotliSize: true
        })
    ],
    build: {
        rollupOptions: {
            output: {
                manualChunks: {
                    react: ['react', 'react-dom', 'react-router-dom'],
                    vaadin: ['@vaadin/react-components'],
                    katex: ['katex']
                }
            }
        }
    },
    optimizeDeps: {
        include: [
            'react',
            'react-dom',
            'katex',
            '@vaadin/react-components'
        ],
        exclude: ['js-big-decimal'] // 불필요한 대형 라이브러리 제외
    },
    css: {
        devSourcemap: true, // CSS 소스맵 최적화
        modules: {
            localsConvention: 'camelCase'
        }
    }
});

export default overrideVaadinConfig(defineConfig(customConfig));
