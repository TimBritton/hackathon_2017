import * as React from 'react';
import './App.css';
import Navbar from './components/navbar/Navbar';
import NavbarMobile from './components/navbar/NavbarMobile';
import PageContent from './components/content/PageContent';
import 'w3-css/w3.css';
import './css/w3-colors-flat.css';
class App extends React.Component {
  render() {
    return (
      <div >
        <Navbar />
        <NavbarMobile />
        <PageContent />
      </div>
    );
  }
}

export default App;
