import abc


class Allocable(abc.ABC):
    @abc.abstractmethod
    def allocate(self, users, gs_lower_bound, gs_upper_bound):
        """Interface method which takes list of users, group size interval
        and return List of groups which contains users"""
        pass
