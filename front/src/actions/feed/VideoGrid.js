import {
    FETCH_VIDEOS_BEGIN,
    FETCH_VIDEOS_SUCCESS,
    FETCH_VIDEOS_FAILURE
} from "../../constants/feed/videoGrid"

export const fetchVideosBegin = genres => ({
    type: FETCH_VIDEOS_BEGIN,
    genres: genres
});

export const fetchVideosSuccess = items => ({
    type: FETCH_VIDEOS_SUCCESS,
    payload: {
        items
    }
});

export const fetchVideosFailure = error => ({
    type: FETCH_VIDEOS_FAILURE,
    payload: {
        error
    }
});
