from main.allocators.allocable import Allocable
import math
import random


class RandomAllocator(Allocable):
    def allocate(self, users, gs_lower_bound, gs_upper_bound):

        number_of_users = len(users)
        number_of_group_lower_bound = int(math.ceil(1.0 * number_of_users / gs_upper_bound))
        number_of_group_upper_bound = int(math.floor(1.0 * number_of_users / gs_lower_bound))
        number_of_group = random.randint(number_of_group_lower_bound, number_of_group_upper_bound)

        random.shuffle(users)
        groups = list()
        for i in range(number_of_group):
            groups.append(list())

        for i in range(gs_lower_bound * number_of_group):

            users[i].set_group_id(i // gs_lower_bound)
            groups[i//gs_lower_bound].append(users[i])

        for i in range(gs_lower_bound * number_of_group, number_of_users):
            group_id = random.randint(0, number_of_group - 1)
            while len(groups[group_id]) >= gs_upper_bound:
                group_id = random.randint(0, number_of_group - 1)
            users[i].set_group_id(group_id)
            groups[group_id].append(users[i])

        return groups
