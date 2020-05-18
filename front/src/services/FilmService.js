import handleErrors from "./handleErrors";
import { fetchVideosSuccess, fetchVideosFailure, fetchVideosBegin } from "../actions/feed/VideoGrid";

const host = "http://localhost:9000";

export const fetchFilms = (dispatch, genres) => {
    const queryParams = genres.map(genre => "genres=" + genre).join("&");
    const path = `${host}/films${queryParams === "" ? "" : "?" + queryParams}`;
    dispatch(fetchVideosBegin());
    return fetch(path,
    {
        headers : { 
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(handleErrors)
    .then(response => response.json())
    .then(json => {
        console.log(path);
        dispatch(fetchVideosSuccess(json));
        return json;
    })
    .catch(error => dispatch(fetchVideosFailure(error)));
};

export const getThumbnail = (id) => `${host}/films/${id}/thumbnail`;

export const getStream = (id) => `${host}/films/${id}/media`;
