import 'font-awesome/css/font-awesome.min.css';
import * as React from 'react';
import 'w3-css/w3.css';
import Note from './notification/Notification';
import { Notification } from './notification/Notification';
import NavbarMobile  from './NavbarMobile';
const nots = [ new Notification('A Notification'), new Notification('A Second Notification') ];
// eslint-disable-next-line
const clzzNameMobile = 'w3-bar-item w3-button w3-hide-medium w3-hide-large' +
 'w3-right w3-padding-large w3-hover-white w3-large w3-theme-d2';
class Navbar extends React.Component {
   
    render() {
        return(
            <div className="w3-top">
                 <div className="w3-bar w3-theme-d2 w3-left-align w3-large">
                 <a className={clzzNameMobile} href="javascript:void(0);" onClick={NavbarMobile.onNav} >
                    <i className="fa fa-bars" />
                    &nbsp;&nbsp;GUILD HOME PRE-ALPHA
                </a>
                    <Note notifications={nots}/>
                 </div>
            </div>
        );
    }
}

export default Navbar;