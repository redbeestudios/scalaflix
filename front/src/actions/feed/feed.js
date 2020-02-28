import { FILTER_BY_MOST_VIEWED, FILTER_BY_LEAST_VIEWED } from "../../constants/feed/filterHeader"

const filterByMostViewed = (id) => (
    {
        key: id,
        type: FILTER_BY_MOST_VIEWED,
    }
);

const filterByLeastViewed = (id) => (
    {
        key: id,
        type: FILTER_BY_LEAST_VIEWED,
    }
);

export default filterByMostViewed;
export default filterByLeastViewed;
