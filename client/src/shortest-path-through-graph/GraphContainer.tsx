import React from 'react';
import classNames from 'classnames';
import InteractiveAdjacencyMatrix from './InteractiveAdjacencyMatrix';
import { connect } from 'react-redux';
import styled from 'styled-components';
import { GraphRequestModel } from './GraphRequestModel';
import { NodeModel } from './NodeModel';
import { fetchShortestPathUsingBFS } from '../shortest-path-through-map/actions';

interface Props {
  postBFS: (graphRequest: GraphRequestModel) => void;
  shortestPath: number[];
  adjacencyMatrix: boolean[][];
  className?: string;
}

export class GraphContainer extends React.Component<Props> {
  render() {
    return (
      <div className={classNames('graph-container', this.props.className)}>
        <InteractiveAdjacencyMatrix graphSize={11}/>
        <button
          className={'bfs-button'}
          onClick={() => this.handleClick()}
        >
          Breadth First Search
        </button>
        {this.renderShortestPath()}
      </div>
    )
  }

  private renderShortestPath() {
    return this.props.shortestPath ?
      <div
        className={'route'}
      >
        Shortest path: {this.pathString(this.props.shortestPath)}
      </div>
      : null;
  }

  private pathString(shortestPath: number[]) {
    return shortestPath.join(', ');
  }

  private handleClick() {
    let graphRequest: GraphRequestModel = new GraphRequestModel(
      this.props.adjacencyMatrix,
      new NodeModel(1)
    );
    this.props.postBFS(graphRequest);
  }
}

const mapStateToProps = (state: any) => ({
  shortestPath: state.shortestPath,
  adjacencyMatrix: state.adjacencyMatrix
});

const mapDispatchToProps = {
  postBFS: fetchShortestPathUsingBFS
};

export default connect(mapStateToProps, mapDispatchToProps)(styled(GraphContainer)`
  button {
    hieght: 100px;
    background: pink;
    cursor: pointer;
    
  }
`);