import React from "react";
import {Component} from "react";

import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/theme-github";

const javaCode = `{
    source.setDistance(0);
 
    Set<Node> settledNodes = new HashSet<>();
    Set<Node> unsettledNodes = new HashSet<>();
 
    unsettledNodes.add(source);
 
    while (unsettledNodes.size() != 0) {
        Node currentNode = getLowestDistanceNode(unsettledNodes);
        unsettledNodes.remove(currentNode);
        for (Entry < Node, Integer> adjacencyPair: 
          currentNode.getAdjacentNodes().entrySet()) {
            Node adjacentNode = adjacencyPair.getKey();
            Integer edgeWeight = adjacencyPair.getValue();
            if (!settledNodes.contains(adjacentNode)) {
                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                unsettledNodes.add(adjacentNode);
            }
        }
        settledNodes.add(currentNode);
    }
    return graph;
}`;
const javaCode2 = `{
    s.setDistance(0);
 
    Set<Node> sn = new HashSet<>();
    Set<Node> un = new HashSet<>();
 
    un.add(s);
 
    while (un.size() != 0) {
        Node cn = getLowestDistanceNode(unsettledNodes);
        un.remove(currentNode);
        for (Entry < Node, Integer> adjacencyPair: 
          cn.getAdjacentNodes().entrySet()) {
            Node adjacentNode = adjacencyPair.getKey();
            Integer edgeWeight = adjacencyPair.getValue();
            if (!sn.contains(adjacentNode)) {
                calculateMinimumDistance(adjacentNode, edgeWeight, cn);
                un.add(adjacentNode);
            }
        }
        settledNodes.add(currentNode);
    }
    return graph;
}`;
const toMarker = marker => ({
  type: "background",
  className: "clone",
  startRow: marker.start.line - 1,
  startCol: marker.start.position,
  endRow: marker.end.line - 1,
  endCol: marker.end.position
});

class SplitView extends Component {
  constructor(props) {
    super(props);
    this.state = {
      leftCode: javaCode,
      rightCode: javaCode2,
      leftMarkers: this.parseMarkers("left", props.markers),
      rightMarkers: this.parseMarkers("right", props.markers),
      markers: props.markers
    };
    this.props.updateData(this.state);
    this.editorLeftRef = React.createRef();
    this.editorRightRef = React.createRef();
  }

  onChange = (editor, newValue) => {
    const code = {};
    code[editor] = newValue;
    this.setState(code);
    this.props.updateData(this.state);
  };

  componentDidUpdate(prevProps) {
    if (prevProps.markers !== this.props.markers) {
      this.handleMarkers();
    }
  }

  handleMarkers() {
    this.setState({
      leftMarkers: this.parseMarkers("left", this.props.markers),
      rightMarkers: this.parseMarkers("right", this.props.markers),
      markers: this.props.markers
    });
  }

  parseMarkers(editor, markers) {
    return markers.map(e => e[editor]).map(toMarker);
  }


  render() {
    return (
      <div className="hero-body columns">
        <div className="is-half column">
          <AceEditor
            mode="java"
            ref={this.editorLeftRef}
            theme="github"
            value={this.state.leftCode}
            onChange={this.onChange.bind(this, 'leftCode')}
            name="left"
            markers={this.state.leftMarkers}
            height="100%"
            width="100%"
            editorProps={{ $blockScrolling: true }}
          />
        </div>
        <div className="is-half column">
          <AceEditor
            mode="java"
            ref={this.editorRightRef}
            theme="github"
            value={this.state.rightCode}
            onChange={this.onChange.bind(this, 'rightCode')}
            name="right"
            markers={this.state.rightMarkers}
            height="100%"
            width="100%"
            editorProps={{ $blockScrolling: true }}
          />
        </div>
      </div>
    );
  }
}

export default SplitView;
