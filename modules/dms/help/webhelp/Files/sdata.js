var gTopicInfo = [
	['Title', 'Topics/IDH_Topic30.htm'],
	['Disclaimer/Copyrights/Trademarks', 'Topics/IDH_Topic20.htm'],
	['Introduction', 'Topics/IDH_Topic40.htm'],
	['Logging on to the Application', 'Topics/IDH_Topic50.htm'],
	['Create File Notification', 'Topics/IDH_Topic10.htm'],
	['Create Inbound file', 'Topics/IDH_Topic60.htm'],
	['Create Internal file', 'Topics/IDH_Topic70.htm'],
	['Create Outbound file', 'Topics/IDH_Topic80.htm'],
	['Search', 'Topics/IDH_Topic90.htm'],
	['Search File Notification', 'Topics/IDH_Topic100.htm'],
	['Create Notification group', 'Topics/IDH_Topic110.htm'],
	['Search Notification group', 'Topics/IDH_Topic120.htm']
];

var gAlphaIndex = [
	['a', 0, 22],
	['b', 23, 30],
	['c', 31, 51],
	['d', 52, 74],
	['e', 75, 91],
	['f', 92, 109],
	['g', 110, 115],
	['h', 116, 122],
	['i', 123, 133],
	['k', 134, 134],
	['l', 135, 147],
	['m', 148, 165],
	['n', 166, 174],
	['o', 175, 188],
	['p', 189, 209],
	['r', 210, 230],
	['s', 231, 260],
	['t', 261, 279],
	['u', 280, 287],
	['v', 288, 289],
	['w', 290, 306],
	['y', 307, 307],
	['z', 308, 308]
];

var gWordList = [
	['ability', [2]],
	['access', [3]],
	['accessed', [3]],
	['accessing', [3]],
	['accountant', [3]],
	['achieve', [2]],
	['active', [4, 10, 11]],
	['address', [3, 4, 5, 7]],
	['addressed', [5]],
	['agents', [1]],
	['aims', [2]],
	['allowed', [4]],
	['amount', [2]],
	['any', [1, 4, 5, 6, 7]],
	['applicable', [4, 5, 6, 7]],
	['application', [3]],
	['applications', [3]],
	['appropriately', [1]],
	['arising', [1]],
	['assigned', [3]],
	['associated', [1, 4, 5, 6, 7, 8, 9]],
	['attach', [2, 4, 5, 6, 7]],
	['auto', [4, 5, 6, 7, 8, 9]],
	['bar', [3, 4, 5, 6, 7, 8, 9, 10, 11]],
	['based', [2, 3, 5, 6, 7]],
	['been', [1, 2]],
	['belongs', [4, 5, 6, 7]],
	['best', [1]],
	['brief', [4, 5, 6, 7]],
	['broadcast', [4]],
	['browser', [3]],
	['capture', [2]],
	['category', [4, 5, 6, 7, 8, 9]],
	['caused', [1]],
	['citizens', [2]],
	['city', [3]],
	['click', [4, 5, 6, 7, 8, 9, 10, 11]],
	['close', [4, 5, 6, 7, 8, 9, 10, 11]],
	['comments', [2, 4, 5, 6, 7]],
	['communication', [2]],
	['company', [1]],
	['concerned', [2, 5, 6, 7]],
	['configured', [4, 5, 6, 7]],
	['consent', [1]],
	['constitute', [1]],
	['copy', [2]],
	['copyrights', [1]],
	['could', [3]],
	['create', [2, 4, 5, 6, 7, 10]],
	['created', [3, 4]],
	['creation', [4, 5, 6, 7, 8]],
	['creator', [4, 5, 6, 7, 8, 9, 10, 11]],
	['damages', [1]],
	['date', [4, 5, 6, 7, 8, 9, 10, 11]],
	['demand', [2]],
	['department', [2, 5, 6, 7, 8, 9, 10]],
	['departments', [2]],
	['described', [4, 5, 6, 7, 8, 9, 10, 11]],
	['description', [4, 5, 6, 7, 8, 9, 10, 11]],
	['designation', [3, 5, 6, 10]],
	['designed', [2]],
	['detailed', [4, 5, 6, 7]],
	['details', [4, 5, 6, 7, 8, 9, 10, 11]],
	['different', [3]],
	['digital', [2]],
	['directors', [1]],
	['disclaimer', [1]],
	['display', [4, 5, 6, 7, 8, 9, 10, 11]],
	['displayed', [3]],
	['dms', [0, 2, 4, 5, 6, 7, 8, 9, 10, 11]],
	['document', [0, 1, 2, 4, 5, 6, 7]],
	['documents', [2]],
	['down', [4, 5, 6, 7, 8, 9, 10, 11]],
	['draft', [5, 6, 7]],
	['drop', [4, 5, 6, 7, 8, 9, 10, 11]],
	['each', [3]],
	['easy', [2]],
	['effective', [10, 11]],
	['effectiveness', [2]],
	['efficiency', [2]],
	['effort', [1, 2]],
	['egov', [0, 2, 3]],
	['egovernments', [0, 1]],
	['either', [3]],
	['electronic', [1]],
	['employee', [2, 3]],
	['employees', [1, 2, 4]],
	['enter', [4, 5, 6, 7, 8, 9, 10, 11]],
	['entered', [4, 5, 6, 7, 8, 9, 10]],
	['exculpation', [1]],
	['existing', [4, 5, 6, 7, 8, 9, 10, 11]],
	['express', [1]],
	['faced', [2]],
	['facilitate', [2, 4, 5, 6, 7]],
	['facility', [2]],
	['features', [2, 3]],
	['field', [4, 5, 6, 7, 8, 9, 10, 11]],
	['file', [2, 4, 5, 6, 7, 8, 9, 10, 11]],
	['files', [2]],
	['financial', [1]],
	['first', [1]],
	['flow', [2]],
	['following', [2, 4, 5, 6, 7, 8, 9, 10, 11]],
	['foregoing', [1]],
	['form', [1]],
	['format', [2]],
	['forward', [2, 5]],
	['forwarded', [2]],
	['foundation', [1]],
	['from', [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]],
	['generated', [2, 4, 5, 6, 7, 8, 9]],
	['government', [2]],
	['greater', [2]],
	['group', [4, 10, 11]],
	['groups', [4]],
	['guidelines', [1]],
	['handling', [2]],
	['heading', [4, 5, 6, 7, 9]],
	['help', [0]],
	['here', [4]],
	['herein', [1]],
	['high', [2, 4, 5, 6, 7, 8, 9]],
	['hyperlink', [5, 6, 7]],
	['illustrate', [3]],
	['important', [3]],
	['inactive', [10, 11]],
	['inbound', [2, 5]],
	['info', [10, 11]],
	['injury', [1]],
	['inter', [2]],
	['internal', [6]],
	['internally', [2]],
	['intra', [2]],
	['introduction', [2]],
	['known', [1]],
	['large', [2]],
	['left', [4, 5, 6, 7, 8, 9, 10, 11]],
	['legal', [1]],
	['level', [2]],
	['liable', [1]],
	['list', [4, 5, 6, 7, 8, 9, 10, 11]],
	['log', [4, 5, 6, 7, 8, 9, 10, 11]],
	['logging', [3]],
	['login', [3]],
	['logo', [1]],
	['logos', [1]],
	['loss', [1]],
	['low', [4, 5, 6, 7, 8, 9]],
	['mail', [4, 5, 7]],
	['managed', [2]],
	['management', [0, 2, 4, 5, 6, 7, 8, 9]],
	['managing', [2]],
	['manual', [2]],
	['marked', [4]],
	['master', [10, 11]],
	['masters', [10, 11]],
	['may', [1]],
	['means', [1]],
	['mechanical', [1]],
	['medium', [4, 5, 6, 7, 8, 9]],
	['members', [1]],
	['mentioned', [1]],
	['module', [4, 5, 6, 7, 8, 9, 10, 11]],
	['movement', [5, 6, 7]],
	['movements', [2]],
	['multiple', [4]],
	['name', [4, 5, 6, 7, 10, 11]],
	['names', [1]],
	['navigation', [4, 5, 6, 7, 8, 9, 10, 11]],
	['new', [2, 4, 5, 6, 7]],
	['non', [1]],
	['not', [1, 11]],
	['note', [3]],
	['notification', [4, 9, 10, 11]],
	['number', [2, 4, 5, 6, 7, 8, 9]],
	['objective', [2]],
	['occurrence', [1]],
	['office', [2]],
	['officers', [1]],
	['online', [0]],
	['org', [0]],
	['organization', [1, 2]],
	['originate', [2]],
	['other', [1, 2]],
	['otherwise', [1]],
	['outbound', [2, 7, 8, 9]],
	['overcome', [2]],
	['overview', [2]],
	['owners', [1]],
	['page', [3]],
	['paperless', [2]],
	['parameters', [2]],
	['part', [1]],
	['perform', [4, 5, 6, 7, 8, 9, 10, 11]],
	['permission', [1]],
	['person', [4, 5, 7]],
	['phone', [4, 5, 7]],
	['photocopying', [1]],
	['physical', [2]],
	['position', [10]],
	['primary', [2]],
	['prior', [1]],
	['priorities', [4, 5, 6, 7, 8, 9]],
	['priority', [4, 5, 6, 7, 8, 9]],
	['problems', [2]],
	['product', [1]],
	['profit', [1]],
	['publication', [1]],
	['publisher', [1]],
	['purpose', [3]],
	['receive', [2, 7]],
	['received', [4, 5, 6, 7, 8, 9]],
	['receiver', [4, 5, 6, 7]],
	['recognized', [1]],
	['record', [3]],
	['recording', [1]],
	['referred', [2]],
	['register', [2]],
	['registered', [1]],
	['related', [1]],
	['release', [0]],
	['reproduced', [1]],
	['reserved', [1]],
	['reset', [4, 5, 6, 7, 8, 9, 10]],
	['respective', [1, 8, 9, 10]],
	['retrieval', [1, 2]],
	['retrieve', [2]],
	['retrieves', [2]],
	['rights', [1]],
	['role', [3]],
	['roles', [3]],
	['said', [1]],
	['save', [5, 6, 7]],
	['schemas', [1]],
	['screen', [3, 4, 5, 6, 7, 8, 9, 10, 11]],
	['search', [2, 4, 5, 6, 7, 8, 9, 10, 11]],
	['searched', [9]],
	['searching', [4, 5, 6, 7]],
	['select', [4, 5, 6, 7, 8, 9, 10, 11]],
	['sender', [4, 5, 6]],
	['senior', [3]],
	['sent', [4, 5, 6, 7, 8, 9]],
	['set', [4, 5, 6, 7, 8, 9, 10, 11]],
	['shall', [1]],
	['shown', [4, 5, 6, 7]],
	['significant', [2]],
	['site', [3]],
	['specially', [2]],
	['specifications', [1]],
	['specify', [4, 5, 6, 7, 8, 9, 10, 11]],
	['spend', [2]],
	['stages', [2]],
	['status', [2, 6, 7, 8, 9]],
	['steps', [4, 5, 6, 7, 8, 9, 10, 11]],
	['storage', [2]],
	['stored', [1]],
	['sub', [4, 5, 6, 7, 8, 9]],
	['submit', [5, 6, 7, 8, 9]],
	['summary', [4, 5, 6, 7]],
	['super', [3]],
	['system', [0, 1, 2, 3, 4, 5, 6, 7]],
	['table', [4, 5, 6, 7, 8, 9, 10, 11]],
	['tag', [4, 5, 6, 7]],
	['tags', [4, 5, 6, 7]],
	['tapaal', [2]],
	['term', [1]],
	['terms', [1]],
	['their', [1]],
	['this', [1, 2, 3, 5, 6, 7]],
	['through', [2]],
	['time', [2]],
	['title', [0]],
	['track', [2]],
	['tracking', [2]],
	['trademarks', [1]],
	['transmitted', [1]],
	['transparency', [2]],
	['type', [3, 5, 6, 7, 8, 9]],
	['typically', [2]],
	['typing', [3]],
	['ulb', [2, 3, 5]],
	['ulbs', [2]],
	['unique', [4, 5, 6, 7, 8, 9]],
	['upto', [11]],
	['url', [3]],
	['use', [1]],
	['user', [3, 4]],
	['users', [4]],
	['values', [4, 5, 6, 7, 8, 9, 10]],
	['various', [2]],
	['ward', [5, 6, 7, 8, 9, 10]],
	['web', [3]],
	['well', [1]],
	['what', [3]],
	['when', [3]],
	['which', [4, 5, 6, 7, 8, 9, 10, 11]],
	['who', [4, 5, 7]],
	['whom', [5]],
	['wide', [3]],
	['will', [2, 4, 5, 6, 7, 10, 11]],
	['within', [3]],
	['without', [1]],
	['workflow', [2]],
	['working', [3]],
	['would', [3]],
	['written', [1]],
	['www', [0]],
	['your', [1]],
	['zone', [5, 6, 7, 8, 9, 10]]
];