import * as React from 'react';
import 'w3-css/w3.css';
import SidebarUser from './UserDisplay/user';
import AccordionMenu from './AccordionMenu/AccordionMenu';
import SidebarHeaderGuild from './SidebarHeader/SidebarHeaderGuild';
class Sidebar extends React.Component {

    render() {
        return (
            <div className="w3-card w3-round w3-white">
                <SidebarHeaderGuild />
                <SidebarUser />
                <hr />
                <AccordionMenu />
            </div>
        );
    }
}

export default Sidebar;