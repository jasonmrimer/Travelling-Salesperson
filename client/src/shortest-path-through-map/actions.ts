import { ActionTypes } from '../actions/ActionTypes';
import { MapModel } from './models/MapModel';
import { GraphRequestModel } from '../shortest-path-through-graph/GraphRequestModel';

function fetchWeightedRouteRequest() {
  return {
    type: ActionTypes.FETCH_WEIGHTED_ROUTE_REQUEST
  };
}


function fetchWeightedRouteSuccess(body: any) {
  return {
    type: ActionTypes.FETCH_WEIGHTED_ROUTE_SUCCESS,
    body
  }
}

function fetchWeightRouteFailure(exception: any) {
  return {
    type: ActionTypes.FETCH_WEIGHTED_ROUTE_FAILURE,
    exception
  }
}

function postNewRouteRequest() {
  return {
    type: ActionTypes.POST_NEW_ROUTE_REQUEST,
  };
}

export function updateMapText(event: any) {
  return {
    type: ActionTypes.UPDATE_MAP_TEXT,
    event
  }
}

export function fetchWeightedRoute() {
  return function (dispatch: any) {
    dispatch(fetchWeightedRouteRequest());
    return fetch('http://localhost:8080/api/weighted-route')
      .then(response => response.json())
      .then(body => dispatch(fetchWeightedRouteSuccess(body)))
      .catch(exception => dispatch(fetchWeightRouteFailure(exception)));
  };
}

export function fetchNewRouteFromText(mapText: string) {
  let map = new MapModel();
  map.serialize(mapText);
  return fetchNewRoute(map);
}

export function toggleMatrix(body: any) {
  return {
    type: ActionTypes.TOGGLE_MATRIX,
    body
  }
}

function fetchNewRoute(map: MapModel) {
  return function (dispatch: any) {
    dispatch(postNewRouteRequest());
    return fetch(
      'http://localhost:8080/api/weighted-route',
      {
        method: 'post',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(map),
      }
    )
      .then(response => response.json())
      .then(body => dispatch(fetchWeightedRouteSuccess(body)))
      .catch(exception => dispatch(fetchWeightRouteFailure(exception)));
  };
}

function postBFSRequest() {
  return {
    type: ActionTypes.POST_BFS_REQUEST
  };
}

function postBFSSuccess(body: any) {
  return {
    type: ActionTypes.POST_BFS_SUCCESS,
    body
  }
}

function postBFSFailure(exception: any) {
  return {
    type: ActionTypes.POST_BFS_FAILURE,
    exception
  }
}

export function fetchShortestPathUsingBFS(graphRequest: GraphRequestModel) {
  return function (dispatch: any) {
    dispatch(postBFSRequest());
    return fetch(
      'http://localhost:8080/api/traverse-graph-with-bfs',
      {
        method: 'post',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(graphRequest),
      }
    )
      .then(response => response.json())
      .then(body => dispatch(postBFSSuccess(body)))
      .catch(exception => dispatch(postBFSFailure(exception)));
  };
}
