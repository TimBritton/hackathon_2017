import 'font-awesome/css/font-awesome.min.css';
import * as React from 'react';
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';

import 'w3-css/w3.css';
// const eventPostCellWidth = {
//     width:"50%"
// };
class EventPost extends React.Component {

    render() {
        return (
            <div className="w3-container w3-card w3-white w3-round w3-margin">
                <button className=" w3-button w3-right w3-opacity w3-margin-top"><i className="fa   fa-bell-slash-o fa-lg" /></button>
                <header className="w3-container">
                    <h6 className="w3-center">V1_EVENT_TAGLINE</h6>
                </header>
                <div className="w3-cell-row">
                    <div className="w3-container w3-left w3-cell w3-half">
                        <div className="w3-center"><DayPicker /></div>
                    </div>
                    <div className="w3-container w3-right w3-cell w3-half">
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent in porttitor est, at cursus lacus. Integer eu leo interdum, fringilla sapien id, pulvinar augue. Vestibulum ut blandit elit. Mauris non lacus viverra, vestibulum ligula non, consectetur turpis. Phasellus ut rutrum metus. Vivamus vitae porttitor velit. Donec mattis lorem tortor. Pellentesque ut lacus nunc. Praesent placerat placerat ligula nec porta. Aliquam rutrum nibh vitae ante viverra, vel ornare augue convallis. Curabitur sit amet interdum massa. Aliquam libero velit, viverra non odio eget, euismod molestie neque. Duis ornare sed massa sit amet molestie. Proin arcu leo, volutpat vitae luctus eget, tincidunt non elit. </p>
                    </div>
                </div>
                <hr className="w3-clear" />
                <footer>
                    <button type="button" className="w3-button w3-theme-d2 w3-right w3-margin-bottom"><i className="fa fa-comment" />  Comment</button>
                    <button type="button" className="w3-button w3-theme-d1 w3-right w3-margin-bottom"><i className="fa fa-thumbs-up" />  Like</button>
                </footer>
            </div>
        );
    }
}
export default EventPost;