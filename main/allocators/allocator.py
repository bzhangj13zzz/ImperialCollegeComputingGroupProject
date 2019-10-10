from main.allocators.allocable import Allocable


class Allocator(Allocable):

    def __init__(self, allocators, evaluator):
        self.allocators = allocators
        self.evaluator = evaluator

    def allocate(self, users, gs_lower_bound, gs_upper_bound):
        """ Return the best result(list of groups, which is list of users) from different algorithms evaluating by
        the metric """

        bestResult = self.allocators[0]

        for allocator in self.allocators[1:]:
            curResult = allocator.allocate(users, gs_lower_bound, gs_lower_bound)
            if self.evaluator.evaluate(bestResult) < self.evaluator.evaluate(curResult):
                bestResult = curResult

        return bestResult



