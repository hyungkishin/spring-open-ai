import React from 'react';
import ReactMarkdown from 'react-markdown';
import remarkMath from 'remark-math';
import rehypeKatex from 'rehype-katex';
import 'katex/dist/katex.min.css';

interface MathNodeProps {
    children: string;
}

const MathNode: React.FC<MathNodeProps> = ({ children }) => {

    console.log("children", children)
    return (
        <ReactMarkdown
            key={children}
            remarkPlugins={[remarkMath]}
            rehypePlugins={[rehypeKatex]}
            components={{
                // @ts-ignore
                math: ({ value }: { value: string }) => (
                    <div className="math-block">{value}</div>
                ),
                inlineMath: ({ value }: { value: string }) => (
                    <span className="math-inline">{value}</span>
                )
            }}
        >
            {children}
        </ReactMarkdown>
    );
};

export default MathNode;
