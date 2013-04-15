from pyquery import PyQuery as pq
import time
import utils
#change the range to (1,2205) to get all A-Z pages
#20 results pr page
recipe_id = 8360
for n in range(418, 2205):

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
		MTime = 0
		HTime = 0
		MpTime = 0
		HpTime = 0

		#temp variables filled
		for this in p('#itemTitle'): RecpName.append(p(this).text())
		for this in p('#lblIngName'): 
			if p(this).text() != '':
				if p(this).attr('class') == 'ingredient-name':
					IngrName.append(p(this).text())
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
		print RecpName
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
		parsetimep = ''
		if Cookphour:
			parsetimep = Cookphour[0]
		HpTime = 0
		MpTime = 0
		for sp in parsetimep.split():
			if sp.isdigit():
				HpTime = int(sp)

		if Cookpmin:
			parsetimep = Cookpmin[0]
		for sp in parsetimep.split():
			if sp.isdigit():
				MpTime = int(sp)



		parsetime = ''
		if Cookthour:
			parsetime = Cookthour[0]
		HTime = 0
		for sp in parsetime.split():
			if sp.isdigit():
				HTime = int(sp)


		parsetime = ''
		if Cooktmin:
			parsetime = Cooktmin[0]
		MTime = 0
		for sp in parsetime.split():
			if sp.isdigit():
				MTime = int(sp)

		#convert all time into minutes
		Ctime = HTime*60 + MTime
		Ptime = HpTime*60 + MpTime
		if RecpName:
			Rname = RecpName[0]
		else:
			Rname = 'null'
		if ImageURL:
			ImgURL = ImageURL[0]
		else:
			ImgURL = 'null'
		
		RecipeDict = { 'id': recipe_id, 'name':Rname, 'url' : nextUrl, 'img_url' : ImgURL, 
			'cook_time' : Ctime, 'wait_time' : Ptime}
		utils.insert('recipe', **RecipeDict)


		IngrDict = {}
		#if len(IngrName) > len(IngrAmnt):
			#print IngrName
			#print IngrAmnt

		for a in range(len(IngrName)):
			if len(IngrAmnt) < len(IngrName):
				IngrAmnt.append('') #in the case where season to preference is the 'ingredient'
				#print len(IngrAmnt)
			IngrDict = { 'recipe_id':recipe_id, 'ingredient_name':IngrName[a], 'quantity':IngrAmnt[a]}
			utils.insert('recipe_ingredient', **IngrDict)

		#--------build the ingredient list---------
		
		




		recipe_id += 1
