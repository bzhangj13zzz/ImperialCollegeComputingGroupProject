from unittest import TestCase

from main.allocators.random_allocator import RandomAllocator
from main.user import User


class TestRandomAllocator(TestCase):

    GS_LOWER_BOUND = 4
    GS_UPPER_BOUND = 6
    USERS_NUMBER = 1000

    def setUp(self):
        self.allocator = RandomAllocator
        self.users = list()
        for i in range(self.USERS_NUMBER):
            self.users.append(User())

    def test_allocate(self):
        random_allocator = RandomAllocator()
        groups = random_allocator.allocate(self.users, self.GS_LOWER_BOUND, self.GS_UPPER_BOUND)

        for (i, group) in enumerate(groups):
            assert self.GS_LOWER_BOUND <= len(group) <= self.GS_UPPER_BOUND
            for user in group:
                assert user.get_group_id() == i
