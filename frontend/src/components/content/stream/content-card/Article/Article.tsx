import 'font-awesome/css/font-awesome.min.css';
import * as React from 'react';
import 'w3-css/w3.css';
export interface ArticleProps {

}
class ArticlePost extends React.Component {

    render() {
        return (
            <div className="w3-container w3-card w3-white w3-round w3-margin">
                <button className=" w3-button w3-right w3-opacity w3-margin-top"><i className="fa fa-bookmark-o fa-lg" /></button>
                <header className="w3-container">
                <h6 className="w3-center">V1_ARTICLE_TITLE</h6>
               </header>              
                <div className="w3-container">
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
                </div>
                <button type="button" className="w3-button w3-right"><span className="w3-opacity">see more&nbsp;</span><i className="fa fa-chevron-down" /> </button>
                <br />
                <hr className="w3-clear" />
                <i className="fa fa-user-circle fa-2x w3-left w3-circle w3-margin-right" />
                <p>V1_POST_USERNAME</p><span className="w3-opacity">V1_Article_Time</span>
                
                <button type="button" className="w3-button w3-theme-d2 w3-margin-bottom w3-right"><i className="fa fa-comment" />  Comment</button>
                <button type="button" className="w3-button w3-theme-d1 w3-margin-bottom w3-right"><i className="fa fa-thumbs-up" />  Like</button>
            </div>
        );
    }
}
export default ArticlePost;