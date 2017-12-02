import * as React from 'react';
import SideBar from './sidebar/SideBar';
import ContentStream from './stream/Stream/ContentStream';
import 'w3-css/w3.css';
const contentStyle = {
    maxWidth: '1400px',
    marginTop: '80px'
};
class PageContent extends React.Component {

    render() {
        return (
            <div className="w3-container w3-content" style={contentStyle}>
                {/* //the grid */}
                <div className="w3-row">
                    {/* //left column */}
                    <div className="w3-col s12 m3">
                        <SideBar />
                    </div>
                    {/* //right column */}
                    <div className="w3-col m7 l9">
                        <ContentStream />
                    </div>
                </div>
            </div>
        );
    }
}
export default PageContent;