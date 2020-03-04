import { SET_CURRENT_FILTER } from "../../constants/feed/FilterMenu";

export const setCurrentFilter = (filter) => {
    return {
      type: SET_CURRENT_FILTER,
      filter
    };
};

