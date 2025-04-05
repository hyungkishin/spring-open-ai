```jsx
import React from "react";
import ReactMarkdown from "react-markdown";
import remarkMath from "remark-math";
import rehypeKatex from "rehype-katex";
import remarkGfm from "remark-gfm";
import "katex/dist/katex.min.css";
import MathJax from "react-mathjax";

// 1. 타입 확장 선언 (모듈 병합 방식)
declare module "react-markdown" {
    export interface Components {
        math?: React.FC<{ children: string }>;
        inlineMath?: React.FC<{ children: string }>;
    }
}

// 2. MathJax 타입 재정의
type MathJaxProps = {
    script?: string | boolean;
    options?: Record<string, any>;
    children: React.ReactNode;
};

// 3. Props 타입
interface MathNodeProps {
    children: string;
}

// 4. 고차 함수 타입 안전성 강화
const mapProps = (props: MathNodeProps) => ({
    ...props,
    remarkPlugins: [
        remarkMath,
        [remarkGfm, {singleTilde: false}]
    ] as const,
    rehypePlugins: [rehypeKatex] as const,
    components: {
        math: ({children}: { children: string }) => (
            <MathJax.Node formula={children}/>
        ),
        inlineMath: ({children}: { children: string }) => (
            <MathJax.Node inline formula={children}/>
        )
    }
});

// 5. 최종 컴포넌트
const MathNode: React.FC<MathNodeProps> = (props) => {
    return (
        <MathJax.Provider
            script="https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-svg.js"
            options={{
                loader: {load: ["input/tex", "output/svg"]},
                tex: {
                    inlineMath: [['$', '$'], ['\\(', '\\)']],
                    displayMath: [['$$', '$$'], ['\\[', '\\]']]
                },
                svg: {
                    fontCache: 'global'
                },
                options: {
                    ignoreHtmlClass: "tex2jax_ignore",
                    processHtmlClass: "tex2jax_process"
                },
                displayAlign: "left",
                displayIndent: "0em",
                processEscapes: true,
                processEnvironments: true,
                processRefs: true,
                processText: true,
                processMath: true,
                processMathJax: true,
                processMathML: true,
                processSVG: true,
                processSVGBlock: true,
                processSVGInline: true,
                processSVGInlineBlock: true,
            }}
        >
            <ReactMarkdown {...mapProps(props)} />
        </MathJax.Provider>
    );
};

export default MathNode;
```