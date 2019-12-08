import React, {Component} from 'react';
import './App.css';
import SplitView from './SplitView';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = { code: {}, markers: [] };
  }

  updateCode(code) {
    this.setState({ code });
  }

  onClick(e) {
    e.preventDefault();
    const _this = this;
    fetch('/api/comparisons', {
      method: 'POST',
      body: JSON.stringify({
        leftCode: `${this.state.code.leftCode}`,
        rightCode: `${this.state.code.rightCode}`
      }), // data can be `string` or
      // {object}!
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(response => {
      response.json().then(function (markers) {
        _this.setState({ markers });
      });
    });
  }

  render() {
    const markers = this.state.markers;
    return (
      <div className="app">
        <div className="hero-head">
          <nav className="level">
            <div className="level-left">
              <div className="level-item">
                <h1 className="subtitle is-5">
                  Code Clones
                </h1>
              </div>
            </div>

            <div className="level-right">
              <button className="button"
                      onClick={this.onClick.bind(this)}>Compare
              </button>
            </div>
          </nav>
        </div>
        <SplitView
          className="hero-body"
          markers={markers}
          updateData={this.updateCode.bind(this)}
        />
      </div>
    );
  }
}

export default App;
