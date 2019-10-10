class User:
    def __init__(self):
        self.group_id = -1

    def set_group_id(self, group_id):
        self.group_id = group_id

    def get_group_id(self):
        return self.group_id