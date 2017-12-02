import 'font-awesome/css/font-awesome.min.css';
import * as React from 'react';
import 'w3-css/w3.css';
import ArticlePost from '../content-card/Article/Article';
import EventPost from '../content-card/Event/EventPost';
import BlurbPost from '../content-card/Blurb/Blurb';
class ContentStream extends React.Component {

    render() {
        return (
            <div>
                <ArticlePost />
                <EventPost />
                <BlurbPost />
            </div>
        );
    }
}
export default ContentStream;