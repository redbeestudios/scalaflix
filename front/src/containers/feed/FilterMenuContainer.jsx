import { setCurrentFilter } from '../../actions/feed/FilterMenu';
import FilterMenu from "../../components/feed/FilterMenu";
import { connect } from 'react-redux';

const mapStateToProps = (
    state
) => {
    return {
        currentFilter: state.currentFilter
    };
};

const mapDispatchToProps = dispatch => {
    return {
        setFilter: (filter) => {
            return () => dispatch(setCurrentFilter(filter))
        }
    };
};

const FilterMenuContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(FilterMenu);

export default FilterMenuContainer;
