import {TOGGLE_GENRE} from "../../constants/feed/FilterMenu";

const toggleGenre = (genre) => {
    return {
        type: TOGGLE_GENRE,
        genre
    };
};

export default toggleGenre;
