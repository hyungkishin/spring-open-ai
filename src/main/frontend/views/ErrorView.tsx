import {ViewConfig} from "@vaadin/hilla-file-router/types.js";

export default function ErrorView() {
    return <div>Page not found</div>;
}

export const config: ViewConfig = {
    route: '*',
    menu: {
        exclude: true,
    },
};