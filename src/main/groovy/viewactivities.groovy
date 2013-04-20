now = new Date()
request.setAttribute('date', now)

// Go to the view 'activities.gtpl'.
forward('activities.gtpl')