import setCurrentFilter from '../../actions/feed/filterHeader';
import FilterButton from '../../components/feed/FilterButton';
import { connect } from 'react-redux';

const mapStateToProps = (
    state,
    ownProps
) => {
    return {
        active: ownProps.filter === state.currentFilter
    };
};

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        onClick: () => dispatch(
            setCurrentFilter(ownProps.filter)
        )
    };
};

const FilterButtonContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(FilterButton);

export default FilterButtonContainer;
