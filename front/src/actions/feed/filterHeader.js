import { SET_CURRENT_FILTER } from "../../constants/feed/filterHeader";

const setCurrentFilter = (filter) => {
    return {
      type: SET_CURRENT_FILTER,
      filter
    };
};

export default setCurrentFilter;