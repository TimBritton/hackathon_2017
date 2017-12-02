import 'font-awesome/css/font-awesome.min.css';
import * as React from 'react';
import 'w3-css/w3.css';

class NavbarMobile extends React.Component {
    /**
     * onNav
     */
    public static onNav() {
        var x = document.getElementById('navDemo');
        if (x != null) {
        if ( x.className.indexOf( 'w3-show' ) === -1 ) {
            x.className += ' w3-show';
        } else { 
            x.className = x.className.replace(' w3-show', '');
        }
    }
    }
     render() {
         return(
             <div className="w3-bar-block w3-theme-d2 w3-hide w3-hide-large w3-hide-medium w3-large">
                <a href="#" className="w3-bar-item w3-button w3-padding-large">Link 1</a>
                <a href="#" className="w3-bar-item w3-button w3-padding-large">Link 2</a>
             </div>
         );
        }
       
}
export default NavbarMobile;