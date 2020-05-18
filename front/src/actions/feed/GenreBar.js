import {
  FETCH_GENRES_BEGIN,
  FETCH_GENRES_FAILURE,
  FETCH_GENRES_SUCCESS,
  TOGGLE_GENRE
} from "../../constants/feed/GenreBar";

export const fetchGenresBegin = () => ({
  type: FETCH_GENRES_BEGIN
});

export const fetchGenresSucess = items => ({
  type: FETCH_GENRES_SUCCESS,
  payload: {
    items
  }
})

export const fetchGenresFailure = error => ({
  type: FETCH_GENRES_FAILURE,
  payload: {
    error
  }
})

export const toggleGenre = (genre) => {
  return {
      type: TOGGLE_GENRE,
      genre
  };
};
