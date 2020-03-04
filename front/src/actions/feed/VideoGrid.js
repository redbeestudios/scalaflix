import { FETCH_VIDEOS } from "../../constants/feed/videoGrid"

const videoGrid = (filter, genres) => {
    return {
        type: FETCH_VIDEOS,
        filter,
        genres
    };
};

export default videoGrid;
