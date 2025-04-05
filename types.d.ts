import * as React from "react";
import "react-markdown";

declare module '*.module.css' {
  declare const styles: Record<string, string>;
  export default styles;
}
declare module '*.module.sass' {
  declare const styles: Record<string, string>;
  export default styles;
}
declare module '*.module.scss' {
  declare const styles: Record<string, string>;
  export default styles;
}
declare module '*.module.less' {
  declare const classes: Record<string, string>;
  export default classes;
}
declare module '*.module.styl' {
  declare const classes: Record<string, string>;
  export default classes;
}

/* CSS FILES */
declare module '*.css';
declare module '*.sass';
declare module '*.scss';
declare module '*.less';
declare module '*.styl';

/* IMAGES */
declare module '*.svg' {
  const ref: string;
  export default ref;
}
declare module '*.bmp' {
  const ref: string;
  export default ref;
}
declare module '*.gif' {
  const ref: string;
  export default ref;
}
declare module '*.jpg' {
  const ref: string;
  export default ref;
}
declare module '*.jpeg' {
  const ref: string;
  export default ref;
}
declare module '*.png' {
  const ref: string;
  export default ref;
}
declare module '*.avif' {
  const ref: string;
  export default ref;
}
declare module '*.webp' {
  const ref: string;
  export default ref;
}
declare module '*.css?inline' {
  import type { CSSResultGroup } from 'lit';
  const content: CSSResultGroup;
  export default content;
}

declare module 'csstype' {
  interface Properties {
    [index: `--${string}`]: any;
  }
}

declare module "react-markdown" {
  interface Components {
    /**
     * 블록 수식 컴포넌트 ($$...$$)
     * @param value - KaTeX 수식 문자열
     */
    math?: React.FC<{ value: string }>;

    /**
     * 인라인 수식 컴포넌트 ($...$)
     * @param value - KaTeX 수식 문자열
     */
    inlineMath?: React.FC<{ value: string }>;
  }
}
