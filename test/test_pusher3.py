import eml_pusher.pusher3 as object_under_test


class ThingWithValue(object):
    def __init__(self, val):
        self.val = val

    def value(self):
        return self.val


class MockPackage(object):
    def __init__(self, identifier, checksum):
        self.identifier = ThingWithValue(identifier)
        self.checksum = ThingWithValue(checksum)


class MockLogger(object):
    def debug(self, msg):
        pass
    def info(self, msg):
        pass


class MockComponent(object_under_test.Component):
    _sysmeta_generator = {} # it's not None


def component_constructor():
    source = None
    return MockComponent(source)


def test_find_all_latest_versions_01():
    """Can we handle a super simple case where there is only one version of each?"""
    iterable = (
            MockPackage('record1.20180101', 'abc111'),
            MockPackage('record2.20180202', 'abc222'))
    result = object_under_test.find_all_latest_versions(iterable, component_constructor, MockLogger(), len(iterable))
    assert len(result) is 2
    assert result['record1'].timestamp() == 20180101
    assert result['record2'].timestamp() == 20180202


def test_find_all_latest_versions_02():
    """Can we handle multiple versions in order?"""
    iterable = (
            MockPackage('record1.20160101', 'abc111'),
            MockPackage('record2.20160202', 'abc221'),
            MockPackage('record1.20170101', 'abc112'),
            MockPackage('record2.20170202', 'abc222'),
            MockPackage('record1.20180101', 'abc113'),
            MockPackage('record2.20180202', 'abc223'),
            )
    result = object_under_test.find_all_latest_versions(iterable, component_constructor, MockLogger(), len(iterable))
    assert len(result) is 2
    assert result['record1'].timestamp() == 20180101
    assert result['record2'].timestamp() == 20180202


def test_find_all_latest_versions_03():
    """Can we handle multiple versions out of order?"""
    iterable = (
            MockPackage('record2.20170202', 'abc222'),
            MockPackage('record2.20160202', 'abc221'),
            MockPackage('record1.20180101', 'abc113'),
            MockPackage('record1.20160101', 'abc111'),
            MockPackage('record1.20170101', 'abc112'),
            MockPackage('record2.20180202', 'abc223'),
            )
    result = object_under_test.find_all_latest_versions(iterable, component_constructor, MockLogger(), len(iterable))
    assert len(result) is 2
    assert result['record1'].timestamp() == 20180101
    assert result['record2'].timestamp() == 20180202


def test_find_all_latest_versions_04():
    """Can we handle multiple versions out of order?"""
    iterable = (
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20150515', 'abc111'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20150723', 'abc112'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20160202', 'abc113'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20160629', 'abc114'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20160804', 'abc115'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20161124', 'abc116'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20170320', 'abc117'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20170515', 'abc118'),
            )
    result = object_under_test.find_all_latest_versions(iterable, component_constructor, MockLogger(), len(iterable))
    assert len(result) is 1
    assert result['aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006'].timestamp() == 20170515


def test_find_all_latest_versions_05():
    """Is the expected exception thrown when the total object count doesn't match?"""
    iterable = (
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20150515', 'abc111'),
            MockPackage('aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20170717', 'abc112'),
            )
    mismatching_total_count = 5
    try:
        result = object_under_test.find_all_latest_versions(iterable, component_constructor, MockLogger(), mismatching_total_count)
    except object_under_test.InternalError:
        pass
