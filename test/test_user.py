from unittest import TestCase

from main.user import User


class TestUser(TestCase):
    def setUp(self):
        self.user = User()

    def test_set_group_id(self):
        assert self.user.get_group_id() == -1
        self.user.set_group_id(2)
        assert self.user.get_group_id() == 2
        self.user.set_group_id(5)
        assert self.user.get_group_id() == 5
