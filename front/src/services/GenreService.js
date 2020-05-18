import {
  fetchGenresBegin,
  fetchGenresSucess,
  fetchGenresFailure
} from '../actions/feed/GenreBar';

import handleErrors from './handleErrors';

const host = "http://localhost:9000";

const path = `${host}/genres`

export const fetchGenres = dispatch => {
  dispatch(fetchGenresBegin());
  return fetch(path)
    .then(handleErrors)
    .then(res => {
      return res.json();
    })
    .then(json => {
      dispatch(fetchGenresSucess(json));
      return json;
    })
    .catch(error => dispatch(fetchGenresFailure(error)));
};
