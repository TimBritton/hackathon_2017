import 'font-awesome/css/font-awesome.min.css';
import * as React from 'react';
class BlurbPost extends React.Component {

    render() {
        return (
            <div className="w3-container w3-card w3-white w3-round w3-margin">
                <button className=" w3-button w3-right w3-opacity w3-margin-top"><i className="fa fa-star-o fa-lg" /></button>
                <div className="w3-container">
                    <i className="fa fa-comment w3-xxlarge w3-text-grey"></i><br />
                    <p className=" w3-large w3-serif">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec a arcu quis mauris consequat gravida. Nulla facilisi. Nunc lobortis dignissim nibh, sed ultrices massa tempus id. Morbi at pretium nisi. Morbi consectetur ante quis dui congue blandit. Donec ornare tellus libero. Maecenas scelerisque malesuada maximus. Nam cursus justo et neque.
                </p>
                </div>
                <hr />
                <div className="w3-col w3-cell-row">
                    <div className="w3-cell" >
                        <i className="fa fa-user-circle fa-3x w3-left w3-circle w3-margin-right" />
                    </div>
                    <div className="w3-container w3-cell">
                        <div className="" ><p>V1_POST_USERNAME</p></div>
                        <div className="" ><span className="w3-opacity">V1_Article_Time</span></div>
                    </div>
                </div>
            </div>
        );
    }
}
export default BlurbPost;