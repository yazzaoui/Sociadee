#!/usr/bin/env python
#
# Sociadee Server App Engine
# Created by Youssef Azzaoui
# youssef.azzaoui@epfl.ch

import webapp2
import facebook
import urllib2
import base64

from google.appengine.ext import ndb
from google.appengine.ext import blobstore
from webapp2_extras import json

################################################
#
#				Datastore Model 
#
################################################

class User(ndb.Model):

	userId= ndb.IntegerProperty() # Facebook user id 
	facebookToken = ndb.StringProperty()

	firstName = ndb.StringProperty()
	lastName = ndb.StringProperty()
	email = ndb.StringProperty()
	city = ndb.StringProperty()
	profilePicture = ndb.BlobProperty(indexed=False)   #ndb.BlobKeyProperty() can be used

	firstConnection = ndb.DateTimeProperty(auto_now_add=True) # on Creation
	lastConnection = ndb.DateTimeProperty(auto_now=True) #onUpdate/Creation

	lastPosition = ndb.KeyProperty() #reference to SavedLocation

class SavedLocation(ndb.Model):
	userKey = ndb.KeyProperty() #reference to User
	location = ndb.GeoPtProperty()


##################################################

#todo : 

##################################################


def testUserKey(userKey):
	try:
		self.userKey = ndb.Key(urlsafe=self.request.get('userKey'))
		self.user = self.userKey.get()
		if(self.user is None) : 
			self.response.write("yapersone")
		else :
			self.response.write(self.user.firstName)

	except Exception, e:
		self.response.write("error bad encoding")


class Home(webapp2.RequestHandler):
	def get(self):
		self.response.write('Sociadee Server')


class UpdateLoc(webapp2.RequestHandler):
	def post(self):
		try:
			self.userKey = ndb.Key(urlsafe=self.request.get('userKey'))
			self.user = self.userKey.get()
			if(self.user is None) : 
				self.response.write("yapersone")
			else :
				self.response.write(self.user.firstName)

		except Exception, e:
			self.response.write("error bad encoding")
		

class Login(webapp2.RequestHandler):

	def post(self):
		self.token = self.request.get('FBtoken') # facebook token here
		#self.response.write(token)
		if(self.token):
			#test validity
			if(self.testFBTokenValid()):
				#test if in DB so just update TOKEN in DB
				qry = User.query(User.userId == int(self.meObj['id'])).fetch()
				self.response.content_type = 'application/json'

				if(qry):
					self.user=qry[0]
					self.userKey = qry[0].key
					#should update the user
					self.updateUser()
					status = "loged"
				else:
					#self.response.write('Registering') #register
					status = "registered"
					self.registerUser()
				
				response = {
    				'status': status, 
    				'token': self.userKey.urlsafe(),
    				'firstname' : self.firstname,
    				'lastname' : self.lastname,
    				'picture' : base64.b64encode(self.user.profilePicture)
  					}

				self.response.write(json.encode(response))
			else:
				self.response.write('ERROR_INVALID_TOKEN')
		else:
			 self.response.write('ERROR_NO_TOKEN')

	def testFBTokenValid(self):
		self.graph = facebook.GraphAPI(self.token)
		try:
			self.meObj = self.graph.get_object("me",fields="id,last_name,first_name,email,picture.type(large)")
			self.facebookId = int(self.meObj['id'])
			self.firstname = self.meObj['first_name']
			self.lastname = self.meObj['last_name']
			self.email = self.meObj['email']
			self.pictureUrl = self.meObj['picture']['data']['url']
			#self.response.write(self.pictureUrl)
		except Exception, e:
			return False
		return True

	#userKey must be set
	def updateUser(self):
		responsePic = urllib2.urlopen(self.pictureUrl)
		dataPic = responsePic.read()

		self.user.firstName = self.firstname
		self.user.lastName = self.lastname
		self.user.facebookToken = self.token
		self.user.profilePicture = dataPic
		self.user.put()

	def registerUser(self):
		#fetch pciture
		responsePic = urllib2.urlopen(self.pictureUrl)
		dataPic = responsePic.read()
		
		self.user = User(userId=self.facebookId,
			facebookToken=self.token,
			firstName=self.firstname,
			lastName=self.lastname,
			email=self.email,
			profilePicture=dataPic)
		self.userKey = self.user.put()


app = webapp2.WSGIApplication([
	('/', Home),
	('/login', Login),
	('/updateloc',UpdateLoc)
], debug=True)
