from pyquery import PyQuery as pq
import time
import utils
#change the range to (1,2205) to get all A-Z pages
for n in range(1, 3):

	urln = 'http://allrecipes.com/recipes/ViewAll.aspx?Page=' + str(n)
	time.sleep(1)			#Sleeping is done according to the Web Crawling Standards
	S = pq(url=urln)

	templst = []
	for u in S('.rectitlediv'): templst.append(S(u))
	urlist = []
	for u in templst: urlist.append(u.find('a').attr('href'))
	print 'list made ' + str(n)

	for nextUrl in urlist:
		time.sleep(1)		#Sleeping is done according to the Web Crawling Standards
		p = pq(url=nextUrl)
		#Creation of temp variables
		RecpName = []
		IngrName = []
		IngrAmnt = []
		Description = []
		ImageURL = []
		Cookpmin = []
		Cookphour = []
		Cookcmin = []
		Cookchour = []
		Cooktmin = []
		Cookthour = []
		Servings = []
		#temp variables filled
		for this in p('#itemTitle'): RecpName.append(p(this).text())
		for this in p('#lblIngName'): IngrName.append(p(this).text())
		for this in p('#lblIngAmount'): IngrAmnt.append(p(this).text())

		for this in p('span.plaincharacterwrap.break'): Description.append(p(this).text())
		for this in p('#imgPhoto'): ImageURL.append(p(this).attr('src'))

		for this in p('#prepMinsSpan'): Cookpmin.append(p(this).text())
		for this in p('#cookMinsSpan'): Cookcmin.append(p(this).text())
		for this in p('#totalMinsSpan'): Cooktmin.append(p(this).text())

		for this in p('#prepHoursSpan'): Cookphour.append(p(this).text())
		for this in p('#cookHoursSpan'): Cookchour.append(p(this).text())
		for this in p('#totalHoursSpan'): Cookthour.append(p(this).text())

		for this in p('#lblYield'): Servings.append(p(this).text())
		#temp variables dumped into where they need to go
#		print RecpName
#		IngrName
#		IngrAmnt
#		Description
#		ImageURL
#		Cookpmin
#		Cookcmin
#		Cookchour
#		Cooktmin
#		Cookthour


		#parse out the decimal times
		parsetime = Cookthour[0]
		HTime = ''
		MTime = ''
		for sp in parsetime.split():
			if sp.isdigit():
				HTime = int(sp)
		parsetime = Cooktmin[0]
		for sp in parsetime.split():
			if sp.isdigit():
				MTime = int(sp)
		#convert all time into minutes
		Ctime = HTime*60 + MTime

		RecipeDict = { 'name':RecpName[0], 'url' : nextUrl, 'img_url' : ImageURL[0], 
			'cook_time' : Ctime}
		utils.insert('recipe', **RecipeDict)

