import * as React from 'react';
import 'w3-css/w3.css';

class SidebarUser extends React.Component {

    render() {
        return (

            <div className="w3-container">
                <div>
                    <h4 className="w3-center">V1_USERNAME</h4>
                    <p className="w3-center">V1_GUILDTITLE</p>
                </div>
                <p className="w3-center"><i className="fa fa-user-circle fa-5x" /> </p>
            </div>
        );
    }
}

export default SidebarUser;