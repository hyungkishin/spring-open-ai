import {useSignal} from '@vaadin/hilla-react-signals';
import type {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {NavLink} from "react-router";

export const config: ViewConfig = {
  menu: {
    title: 'Main page',
  },
};

export default function MainView() {
  const name = useSignal('');

  return (
    <>
        <li>
            <NavLink to="/open-chat">open chat</NavLink>
        </li>

    </>
  );
}