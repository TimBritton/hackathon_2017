import * as React from 'react';
import 'w3-css/w3.css';
class AccordionMenu extends React.Component {

    render() {
        return (
            <div className="w3-theme-l1">
                <header className="w3-container">
                    <h4>Header</h4>
                </header>
                <hr />
                <p><i className="fa fa-pencil fa-fw w3-margin-right w3-text-theme" /> Designer, UI</p>
                <p><i className="fa fa-home fa-fw w3-margin-right w3-text-theme" /> London, UK</p>
                <p><i className="fa fa-birthday-cake fa-fw w3-margin-right w3-text-theme" /> April 1, 1988</p>
            </div>
        );
    }
}

export default AccordionMenu;