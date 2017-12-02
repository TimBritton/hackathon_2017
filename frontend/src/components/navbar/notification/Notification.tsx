import 'font-awesome/css/font-awesome.min.css';
import * as React from 'react';
import 'w3-css/w3.css';

export interface NotificationProps { notifications: Notification[]; }

export class Notification {
    text: String;
    time: Date;
    constructor(text: String) {
        this.text = text;
    }
}
const dropDownStyle = {
    width: '300px'
};
class Note extends React.Component<NotificationProps, {}> {
    render() {
        return (
            <div className="w3-dropdown-hover w3-hide-small">
                <button className="w3-button w3-padding-large" title="Notifications">
                    <i className="fa fa-bell" />
                    <span className="w3-badge w3-right w3-small w3-green">{this.props.notifications.length}</span>
                </button>
                <div className="w3-dropdown-content w3-card-4 w3-bar-block" style={dropDownStyle} >
                    {

                        this.props.notifications.map((element, index) => {
                            return <a key={index} href="#" className="w3-bar-item w3-button">{element.text}</a>;
                        })
                    }
                </div>
            </div>
        );
    }
}

export default Note;
