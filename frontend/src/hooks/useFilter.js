import { useState, useEffect } from 'react';
import { getLocalStorage, setLocalStorage } from '../utils/helpers';
import { FILTER_STORAGE_KEY } from '../utils/constants';

/**
 * Custom hook for managing filter state with localStorage persistence.
 * @param {string} key - Unique key for this filter set
 * @param {Object} defaultFilters - Default filter values
 * @returns {Object} Filter state and setters
 */
const useFilter = (key, defaultFilters = {}) => {
  const storageKey = `${FILTER_STORAGE_KEY}_${key}`;
  
  const [filters, setFilters] = useState(() => {
    const stored = getLocalStorage(storageKey);
    return stored || defaultFilters;
  });

  // Persist filters to localStorage whenever they change
  useEffect(() => {
    setLocalStorage(storageKey, filters);
  }, [filters, storageKey]);

  /**
   * Update a single filter value.
   * @param {string} filterKey - Filter key to update
   * @param {any} value - New value
   */
  const updateFilter = (filterKey, value) => {
    setFilters((prev) => ({
      ...prev,
      [filterKey]: value,
    }));
  };

  /**
   * Update multiple filters at once.
   * @param {Object} newFilters - New filter values
   */
  const updateFilters = (newFilters) => {
    setFilters((prev) => ({
      ...prev,
      ...newFilters,
    }));
  };

  /**
   * Reset filters to default values.
   */
  const resetFilters = () => {
    setFilters(defaultFilters);
  };

  /**
   * Clear all filters (empty object).
   */
  const clearFilters = () => {
    setFilters({});
  };

  return {
    filters,
    updateFilter,
    updateFilters,
    resetFilters,
    clearFilters,
    setFilters,
  };
};

export default useFilter;
